import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import { Client } from '@stomp/stompjs';
import { FaPaperclip, FaFile, FaThumbtack } from 'react-icons/fa';
import '../styles/chat.css';

const Chat = () => {
  const user = { id: 2, username: 'Jane Smith', userType: 'Patient' };
   //const user = { id: 1, username: 'John Doe', userType: 'Physiotherapist', specialty: 'Orthopedics' } // Changed from Doctor to Physiotherapist
  //const user = { id: 3, username: 'MaryJones', userType: 'Physiotherapist', specialty: 'Neurologist' } // Changed from Doctor to Physiotherapist
 
  const [contacts, setContacts] = useState([]);
  const [selectedContact, setSelectedContact] = useState(null);
  const [messages, setMessages] = useState([]);
  const [pinnedMessage, setPinnedMessage] = useState(null);
  const [newMessage, setNewMessage] = useState('');
  const [selectedFile, setSelectedFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const stompClientRef = useRef(null);
  const currentContactRef = useRef(null);
  const fileInputRef = useRef(null);
  const messagesContainerRef = useRef(null); // Ref for chat-messages container

  // WebSocket setup
  useEffect(() => {
    const client = new Client({
      brokerURL: 'ws://localhost:8081/chat-websocket',
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      debug: (str) => console.log('STOMP Debug:', str),
    });

    client.onConnect = () => {
      console.log('Connected to WebSocket');
      stompClientRef.current = client;
      client.subscribe(`/topic/messages/${user.id}`, (message) => {
        console.log('Received message:', message.body);
        const receivedMessage = JSON.parse(message.body);
        setMessages((prevMessages) => {
          if (prevMessages.some((msg) => msg.id === receivedMessage.id)) return prevMessages;
          if (
            selectedContact &&
            (receivedMessage.senderId === selectedContact.id ||
              receivedMessage.receiverId === selectedContact.id ||
              receivedMessage.senderId === user.id)
          ) {
            const updatedMessages = [...prevMessages, receivedMessage];
            // Auto-scroll to bottom when receiving a new message
            setTimeout(() => {
              if (messagesContainerRef.current) {
                messagesContainerRef.current.scrollTop = messagesContainerRef.current.scrollHeight;
              }
            }, 0);
            return updatedMessages;
          }
          return prevMessages;
        });
      });
    };

    client.onDisconnect = () => console.log('Disconnected from WebSocket');
    client.onStompError = (frame) => console.error('STOMP Error:', frame.headers?.message, frame.body);
    client.onWebSocketError = (error) => console.error('WebSocket Low-Level Error:', error);
    console.log('Activating STOMP client...');
    client.activate();

    return () => {
      if (client.connected) {
        console.log('Deactivating STOMP client...');
        client.deactivate();
      }
    };
  }, [user.id, selectedContact]);

  // Fetch contacts
  useEffect(() => {
    const fetchContacts = async () => {
      try {
        let endpoint = user.userType === 'Patient' 
          ? 'http://localhost:8081/api/physiotherapists' 
          : 'http://localhost:8081/api/patients';
        console.log('Fetching contacts from:', endpoint);
        const response = await axios.get(endpoint);
        console.log('Contacts fetched:', response.data);
        setContacts(response.data);
      } catch (error) {
        console.error('Error fetching contacts:', error.response?.status, error.response?.data || error.message);
      }
    };
    fetchContacts();
  }, [user.userType, user.id]);

  // Fetch messages
  useEffect(() => {
    if (!selectedContact) return;

    const fetchMessages = async () => {
      currentContactRef.current = selectedContact;
      setLoading(true);
      setMessages([]);
      setPinnedMessage(null);
      try {
        const response = await axios.get(`http://localhost:8081/api/chat/${user.id}/${selectedContact.id}`, {
          params: { senderType: user.userType, receiverType: selectedContact.userType },
        });
        if (currentContactRef.current === selectedContact) {
          setMessages(response.data);
          // Auto-scroll to bottom after fetching messages
          setTimeout(() => {
            if (messagesContainerRef.current) {
              messagesContainerRef.current.scrollTop = messagesContainerRef.current.scrollHeight;
            }
          }, 0);
        }
      } catch (error) {
        console.error('Error fetching messages:', error);
        setMessages([]);
      } finally {
        if (currentContactRef.current === selectedContact) {
          setLoading(false);
        }
      }
    };
    fetchMessages();
  }, [selectedContact, user.id, user.userType]);

  // Handle file attachment
  const handleAttachClick = () => fileInputRef.current.click();

  const handleFileChange = async (e) => {
    const file = e.target.files[0];
    if (!file || !selectedContact) return;

    setSelectedFile(file);
    const formData = new FormData();
    formData.append('senderId', user.id);
    formData.append('senderType', user.userType);
    formData.append('receiverId', selectedContact.id);
    formData.append('receiverType', selectedContact.userType);
    formData.append('content', newMessage || '');
    formData.append('file', file);

    try {
      await axios.post('http://localhost:8081/api/chat/send', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });
      setNewMessage('');
      setSelectedFile(null);
      fileInputRef.current.value = '';
      // Auto-scroll to bottom after sending file
      if (messagesContainerRef.current) {
        messagesContainerRef.current.scrollTop = messagesContainerRef.current.scrollHeight;
      }
    } catch (error) {
      console.error('Error sending file:', error);
    }
  };

  // Send text message
  const handleSendMessage = async (e) => {
    e.preventDefault();
    if (!newMessage.trim() || !selectedContact) return;

    const formData = new FormData();
    formData.append('senderId', user.id);
    formData.append('senderType', user.userType);
    formData.append('receiverId', selectedContact.id);
    formData.append('receiverType', selectedContact.userType);
    formData.append('content', newMessage);

    try {
      await axios.post('http://localhost:8081/api/chat/send', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });
      setNewMessage('');
      // Auto-scroll to bottom after sending message
      if (messagesContainerRef.current) {
        messagesContainerRef.current.scrollTop = messagesContainerRef.current.scrollHeight;
      }
    } catch (error) {
      console.error('Error sending message:', error);
    }
  };

  // Format timestamp
  const formatTimestamp = (timestamp) => {
    const date = new Date(timestamp);
    const year = date.getFullYear();
    const month = date.toLocaleString('default', { month: 'short' });
    const day = date.getDate();
    const hours = date.getHours();
    const minutes = date.getMinutes().toString().padStart(2, '0');
    const ampm = hours >= 12 ? 'PM' : 'AM';
    const formattedHours = hours % 12 || 12;
    return `${year} ${month} ${day} ${formattedHours}:${minutes} ${ampm}`;
  };

  // Check if file is an image
  const isImage = (fileType) => fileType && fileType.toLowerCase().startsWith('image/');

  // Pin message
  const handlePinMessage = (msg) => {
    setPinnedMessage(pinnedMessage && pinnedMessage.id === msg.id ? null : msg);
  };

  // Scroll to pinned message
  const scrollToPinnedMessage = () => {
    if (pinnedMessage && messagesContainerRef.current) {
      const pinnedElement = messagesContainerRef.current.querySelector(`#message-${pinnedMessage.id}`);
      if (pinnedElement) {
        pinnedElement.scrollIntoView({ behavior: 'smooth', block: 'center' });
      }
    }
  };

  return (
    <div className="container-chat">
      <div className="chat-page">
        <h1>Chat as {user.username} ({user.userType})</h1>
        <div className="chat-container">
          <div className="contact-list">
            <label>Select a {user.userType === 'Patient' ? 'Physiotherapist' : 'Patient'} to chat with:</label>
            <select
              value={selectedContact ? selectedContact.id : ''}
              onChange={(e) => {
                const contact = contacts.find((c) => c.id === parseInt(e.target.value));
                setSelectedContact(contact);
              }}
            >
              <option value="">Select...</option>
              {contacts.map((contact) => (
                <option key={contact.id} value={contact.id}>
                  {contact.username} {user.userType === 'Patient' ? `(${contact.specialty || ''})` : ''}
                </option>
              ))}
            </select>
          </div>
          <div className="chat-box">
            {selectedContact ? (
              loading ? (
                <div className="loader">Loading messages...</div>
              ) : (
                <>
                  <div className="chat-header">
                    Chatting with {selectedContact.username} ({selectedContact.userType}
                    {selectedContact.specialty ? ` - ${selectedContact.specialty}` : ''})
                  </div>
                  {pinnedMessage && (
                    <div className="pinned-banner" onClick={scrollToPinnedMessage}>
                      See Pinned Message
                    </div>
                  )}
                  <div className="chat-messages" ref={messagesContainerRef}>
                    {messages.map((msg) => (
                      <div
                        key={msg.id}
                        id={`message-${msg.id}`}
                        className={
                          msg.senderId === user.id
                            ? 'message sent' + (pinnedMessage && pinnedMessage.id === msg.id ? ' pinned' : '')
                            : 'message received' + (pinnedMessage && pinnedMessage.id === msg.id ? ' pinned' : '')
                        }
                      >
                        {msg.content && <p>{msg.content}</p>}
                        {msg.file && msg.filePath && (
                          <div>
                            {isImage(msg.fileType) ? (
                              <a href={`http://localhost:8081/${msg.filePath}`} target="_blank" rel="noopener noreferrer">
                                <img
                                  src={`http://localhost:8081/${msg.filePath}`}
                                  alt={msg.fileName}
                                  style={{ maxWidth: '200px', maxHeight: '200px' }}
                                />
                              </a>
                            ) : (
                              <div style={{ display: 'flex', alignItems: 'center' }}>
                                <FaFile style={{ marginRight: '5px' }} />
                                <a href={`http://localhost:8081/${msg.filePath}`} target="_blank" rel="noopener noreferrer">
                                  {msg.fileName}
                                </a>
                              </div>
                            )}
                          </div>
                        )}
                        <small>{formatTimestamp(msg.timestamp)}</small>
                        <button
                          onClick={() => handlePinMessage(msg)}
                          className={`pin-button ${pinnedMessage && pinnedMessage.id === msg.id ? 'pinned' : ''}`}
                          title={pinnedMessage && pinnedMessage.id === msg.id ? 'Unpin' : 'Pin'}
                        >
                          <FaThumbtack />
                        </button>
                      </div>
                    ))}
                  </div>
                  <form onSubmit={handleSendMessage} className="chat-input" style={{ display: 'flex', alignItems: 'center' }}>
                    <input
                      type="text"
                      value={newMessage}
                      onChange={(e) => setNewMessage(e.target.value)}
                      placeholder="Type your message..."
                      style={{ flex: 1 }}
                    />
                    <button type="button" onClick={handleAttachClick} style={{ margin: '0 10px' }}>
                      <FaPaperclip />
                    </button>
                    <input type="file" ref={fileInputRef} onChange={handleFileChange} style={{ display: 'none' }} />
                    <button type="submit">Send</button>
                  </form>
                </>
              )
            ) : (
              <p>Select a contact to start chatting.</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Chat;
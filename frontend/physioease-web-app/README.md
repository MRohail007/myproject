### Todo
>Here u can find the list of tasks that need to be done in the project and the status of each task.

- [x] Fixed progressData functionality, fetch progress data from the server 
- [x] Implement unskipable fucntionality for video
- [x] Fixed calculate value of progressData for the chart, support smaller values
- [x] When the weekly progress is completed, we want to show a celebration animation using this library [text](https://mateoguzmana.github.io/react-native-fiesta/docs/components/fireworks)
- [x] Add achievements/badges UI for the patient exercises (after every completed exercise there is a motivating card message)
- [x] Make sure the patient has the ablity to replay the video
- [x] Add a Checkbox to mark video as completed also
> Only after a video is played and the videoId has been extracted from the video URL then the 
> video can be marked as completed

- [x] Celebration also need to end after 5 seconds
- [x] Clean up code base for better readability
- [x] Clean up user, patient and Physiotherapist classes
- [x] Have an upload button under each video exercise to upload the video to the server(Physiotheraspist)
- [x] Clean up patient dashboard codebase and modularize the codebase
- [x] Implement the upload video functionality for the patiemt using the created backend upload endpoint. 
  - [ ] File Preview : Display a preview of the selected video file (e.g., thumbnail or file name).
- [ ] Patient need to also earn badge for video upload after 5 video upload
- [x] Automically save default new patient achivement/badges to database on registation or exercise update
- [ ] Delete videos after one month of upload
TODO: 
- [x] Need a better way to handle the video status (Locked, Unlocked, Completed) from the server
For example, if the video is completed the status has to be updated on the server and the video should be locked, and 
when a video play button is clicked the status should be updated to unlocked by sending a request to the server which
with then unlock the video. Also a skipped button so when the button is clicked the video status is updated to skipped.

- [x] Handle Video Recording upload
Currently i am having issue with the onRecordingComplete, this is because when the recording is complete we want to be able
to send a post request to the server.
Solved: This was handled using a button, because the webcam needs to be triggered by a button so when the video close it doesnt automicatically stop 
the video so the post request wasnt sent so now we have a way to send the post request when we click on the stop recording, which leans to the new task.
- [ ] When we close the video we dont want to send the recording, we only what to send the recording when we click on submit
- [ ] Making sure the user clicks on submit before the video ends because when the video ends it hides the submit button. 

- [x] Restrict the physiotherapist video upload to just 7 weekly  so we can have a total of 35 videos and we have to fetch video from the server based on the week or date.
- [ ] Finish the flowchart design for each functionality 
- [ ] Create backend methods for handling the video status (Locked, Unlocked, Completed)
---
Instead of using is completed to lock the video, why can we lock the videos based on days so like if its monday other video get locked and if its teusday monday gets locked, teusday gets opened and other days is also locked.
Only when we are in a certain day does the video status change to unlocked.



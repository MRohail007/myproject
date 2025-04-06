## Implementation
Start Date - Date: 03-12-2024

>[!Note]
> Thinking of adding a base users that both users can inherit common fields from, something like an auditable for based 
user class.
So the next thing i want to work on next is to possible identify fields needed for each of the entity, in other to 
implement the video exercise library and also decided with kind of user relation will be suitable for the application. 
Is it work having a base users that both two users can inherit common field from?

- **Tasks**
1. Create Entity Models
   - [x] Identify fields needed for each entity in the video exercise library.
   - [x] Create class for each users [Auditable,User, patient,physiotherapist]
   - [X] Annotate each classes and added JPA Mapping
>[!Note]
> Update: I think i have found a better way to struture the entities for better maintainability. 
> In this structure both the Patient and Physiotherapist classes will inherit the auditing fields from the Auditable 
  class through the User class.
  Here is how the inheritance works in this case
*   Auditable Class: This class contains the auditing fields (e.g., createdBy, createdDate, lastModifiedBy, lastModifiedDate).
  *   User Class: This class extends Auditable, meaning it inherits all the fields and methods from Auditable.
    *   Patient and Physiotherapist Classes: Both of these classes extend User , which means they also inherit all the fields 
    *   and methods from both User and Auditable.

2. Configure and Setup backend application with database using dockers

- [x] Connect application with postgres database and Pgadmin
  - [x] Create Docker compose file for postgres database
  - [x] Create .env to hold the credential for the database
  - [x] Configure and connect database with backend services
- [x] Created dummy data for all users
  - [x] Define the schema based on the identified fields and inheritance structure.
  - [x] Manually input user data into database

>[!Note]
Now that we have our model and database setup, we need to run test the connection so we have to try to get a user from
> the database by creating a `getUser` for both the patient and the physiotherapist.

3. Test connection with database (Create endpoint to getUser)
- [x] Create Service and Repository for Users (Patient and Physiotherapist).
- [x] Create controller for user (Patient and Physiotherapist)
- [x] Test endpoint
>[!Note]
Endpoint tested, now we can get both the patient and the physiotherapist by their id, so now we will proceed working on 
> the main sprint task(VideoLibrary)

4. Work on chosen task Sprint one (Break task down into smaller chunks) |
**Exercise Resources & Video Library**
- [x] Backend | Design Video Entity Model
  - [x] Create video entity model
  - [x] Map video model with users (patient and physiotherapist)
- [x] Backend | Video Core Service Layer
  - [x] Create Services for Business Logic
  - [x] Create Controller endpoint
    - [x] Create physiotherapist video upload endpoints. 
    - [x] Create endpoint to get all videos
    - [x] Create endpoint to get all video assigned to physiotherapist
- [x] Frontend | Patient Dashboard and Video Library Wife-frame Design
- [x] Frontend | Patient Dashboard
- [x] Frontend Development |Video Player Component
- [ ] Todo
  - Add additional note to video exercise (for other tips)
    - Add note files to video entity
  - Perfect the exercise tracking chart
    - Add smaller values for completed videos

5. Work on chosen task **Sprint Two** (Break down into smaller chunck) |
**Physiotherapist Search and Initial Assessment**
- [x] Backend | Develop the backend endpoint for getting all physiotherapist
    - [x] Create endpoints to GET all physiotherapist
    - [x] Create endpoints to GET all physiotherapist by specialization
    - [x] Create endpoints to GET all physiotherapist by location
- [ ] Backend | Develop backend logic and endpoint for specialization-based filtering with Spring Boot

6. Work on User Management Task
- [x] Create UserType Enum class



## BACKEND TODO
- [ ] Find a way to calculate the completion value for all videos or eaCh video (When a video is complete i want to be derive a value for completed so i can use on the chart)

**Video Progress Workflow**
Scenario :
    A patient has 5 videos in the "Warm-up" category for Exercise Day 1 or the Week.
    Initially, none of the videos are marked as completed.
    The user completes one video (ID: 1).
Frontend send reqeust the mark the video as completed
```json
{
    "id": 1,
    "isCompleted": true
}
```
- The backend processes the request:
    Marks the video with ID 1 as completed.
    Fetches all videos in the "Warm-up" category for Exercise Day 1.
    Calculates the completion percentage:
        Total videos: 5
        Completed videos: 1
        Completion percentage: 51​×100=20%

  
The completedValue field for the video is updated to 20%.
The updated video is saved in the database. 
     
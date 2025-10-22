# dTasks
A simple task manager app

## Building and running the app.
- You will need android studio and an emulator or a physical device
- Clone the repository and import to android studio
- Setup the baseUrl under `gradle.properties` (more on this below)
- Hit the run icon 
## Starting the mock server.
- Install mock server
```bash
npm install -g json-server
```
- Open the terminal and cd to `project/db` then run the following command
```bash
json-server --watch db.json --port 3000
```
The server will run at : `http://localhost:3000`
- Find a way to access this from your device, I used [ngrok](https://ngrok.com/) to forward the server.
  - Once you have ngrok installed, just run
  ```bash
  ngrok http 3000
  ```
- Add the baseUrl under `gradle.properties`
## Architecture overview.
## Conflict resolution strategy.
## Running tests.
## Kwown Issues & Improvements
# My Notes App

MyNotes is an Android app that enables users to securely create, edit, delete, and view notes using Google Authentication and Cloud Firestore. Users can sign in with Google or Email and manage their notes in a simple, intuitive interface. The app is built with a modular approach, following best practices in Android development.

## Features 

- **User Authentication**: Secure Google sign-in integration and verified Email login using Firebase Authentication.

- **Note Management**: Create, edit, delete, and view notes.

- **Persistent Data Storage**: Notes are stored in Firebase Firestore, ensuring data persistence and accessibility across devices.

- **Staggered Layout**: Displays notes in a staggered grid layout for better visibility.

- **Offline Support** : Allows users to perform CRUD operations on notes without internet. When online, the firestore database automatically updates.

## Screenshots 

**Sign In, Sign Up, Forget Password**
<img src="https://github.com/user-attachments/assets/00744457-a88e-469b-9a32-8a644818cf22" width="200" height="450">
<img src="https://github.com/user-attachments/assets/91edc961-f3e5-4b07-be2a-e1ca17d1f0fb" width="200" height="450">
<img src="https://github.com/user-attachments/assets/49b73f75-3efd-4f4b-972d-fb0e486426da" width="200" height="450">

**In Dark Mode of user's device**

<img src="https://github.com/user-attachments/assets/69c08540-fa13-4b18-808d-75efdc8c2661" width="200" height="450">
<img src="https://github.com/user-attachments/assets/cba2ce74-5eab-4bd7-a580-f389c3fcb8a4" width="200" height="450">
<img src="https://github.com/user-attachments/assets/e0de8487-72c9-404d-a9c1-1a517ede11a0" width="200" height="450">
<img src="https://github.com/user-attachments/assets/d4b5f1c4-e628-4b76-8aaf-6a356c701dd6" width="200" height="450">

**In Light Mode of user's device**

<img src="https://github.com/user-attachments/assets/2ca55e35-d886-4bbe-8b46-3ffd4ca8d250" width="200" height="450">
<img src="https://github.com/user-attachments/assets/350d1d00-41ae-48c9-9f6f-9ca4ab23b4f9" width="200" height="450">

## Project Structure 
- **MainActivity**: Entry point of the app that loads the home fragment or login fragment based on the user's authentication status.
- **LoginPage**: Fragment for Google Sign-In using Firebase Authentication.
- **HomeFragment**: Displays a list of notes. Users can view, edit, or delete notes.
- **CreateNoteFragment**: Allows users to create new notes.
- **EditNoteFragment**: Allows users to edit existing notes.
- **NoteDetails**: Fragment displaying the details of a selected note.

## TechStack 
XML, Kotlin, Android Studio, Firebase

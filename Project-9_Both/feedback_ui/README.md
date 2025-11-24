# Provider Feedback Portal – Frontend (Team Avengers)

**THE PROJECT MAY NEED CERTAIN PATHS UPDATED AS THE NAME AND PATH WAS UPDATED -- CODE MAY NOT RUN UNTIL UPDATED**

This repository contains the **React frontend** for the Provider Feedback Portal. It lets members submit feedback about healthcare providers and view existing feedback by calling the separate **Feedback API** service.

## 🛠 Tech Stack

- **React 19** 
- **Vite** 
- **Tailwind CSS**
- **Axios** 
- **React Router**
- **Node 18+ required (Node 20+ recommended)**

## 📖 Overview

The Provider Feedback Portal frontend is a React application that allows members to submit feedback about healthcare providers and view existing feedback. It communicates with the separate **Feedback API** service to create new feedback entries and to retrieve feedback by ID or by member ID.

This UI is built with **React + Vite + Tailwind**, and focuses on a clean, simple user experience for entering and viewing provider feedback.

## ✨ Features

- **Submit Feedback Form** – members can enter provider name, rating (1–5), and comments.
- **Client-side Validation** – ensures required fields are filled and rating is within 1–5.
- **View All Feedback** – displays a list of all feedback returned from the API.
- **Search by Feedback ID** – fetch a single feedback entry by its UUID.
- **Search by Member ID** – retrieve all feedback associated with a specific member.
- **Responsive UI** – built with Tailwind CSS for a clean and modern layout.
- **React Router Navigation** – simple page routing between form and list views.
- **Axios Integration** – handles API requests to the Feedback API backend.

## 🚀 Installation & Running the App

1. **Clone the repository**
```
   git clone <repository-url>
   cd tsg-9.27-TheAvengers-frontend-feedback-ui
```
2. **Install dependencies**
```bash
npm install

```
3. **Start the development server**
```bash
npm run dev

```
4. **Open the app in your browser**
```bash
http://localhost:5173

```

This frontend expects the **Feedback API** backend to be running (commonly at `http://localhost:8080`).  
See the backend repository for Docker and API setup instructions.

## 📍 Application Routes

The frontend uses **React Router** to provide simple navigation between pages:

- **`/feedbackForm`** – Page for submitting new provider feedback.
- **`/feedbackList`** – Displays a list of all feedback returned from the API.
- **`/feedbackById`** – Search and view a single feedback entry by its UUID.
- **`/feedbackByMemberId`** – Search and view all feedback for a specific member.

Navigation links are provided in the app header to switch between these pages.

## 🔌 Backend Dependency

This frontend relies on the separate **Feedback API** backend service to handle data storage and event publishing.

The UI sends all feedback requests to:

http://localhost:8080/api/v1/feedback


To submit or retrieve feedback successfully, ensure the backend service is running.  
The backend provides its own Dockerfile and `docker-compose.yml` for easy setup.

Backend repository:  
https://github.com/York-Solutions-B2E/tsg-9.27-TheAvengers-feedback-api

## 👥 Team

**The Avengers**  
- Michael Files 
- Gregg Trunnell

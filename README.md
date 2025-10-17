# SafeGuard

SafeGuard is a community-driven platform for cybersecurity enthusiasts to share news, insights, discussions, and resources — similar to Reddit but focused entirely on cybersecurity topics. The app is built using [Javalin](https://javalin.io/) (a lightweight Java web framework) and [Thymeleaf](https://www.thymeleaf.org/) for server-side HTML rendering.

---

## Features

- **User registration and authentication**
- Create, read, update, and delete posts related to cybersecurity
- Comment and reply threads on posts
- Upvote/downvote system to rank posts and comments
- User profiles displaying contribution stats
- Session management with secure cookies
- Role-based access control for moderators and admins
- Responsive UI rendered with Thymeleaf templates

---

## Tech Stack

- **Backend:** Java 17+, [Javalin](https://javalin.io/)
- **Frontend:** Thymeleaf templating engine
- **Persistence:** DAO pattern, can be extended to use databases like PostgreSQL or MongoDB
- **Session & Security:** Jetty session handler, custom AccessManager for route protection
- **Build:** Maven or Gradle

---

## Getting Started

### Prerequisites

- Java 17 or higher installed
- Maven or Gradle build tool
- Git

### Clone the Repository

```bash
git clone https://github.com/yourusername/safeguard.git
cd safeguard

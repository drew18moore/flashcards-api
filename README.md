# flashcards-client

The backend-end for a flashcards mobile app for iOS and Android

### Built With

![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

## Features

- User authentication with JWT
- Persistent user login
- Create, edit, or delete decks
- Create, edit, or delete cards in a deck
- Study your flashcards
- Create custom tests to test your knowledge

## Getting Started
### Prerequisites
- Docker Desktop

Go to https://docs.docker.com/desktop/ and install Docker Desktop

Run the following two commands to ensure that docker and docker compose are properly installed
```
docker -v
docker compose version
```

### Usage
1. Clone the repo
```
git clone https://github.com/drew18moore/flashcards-api.git
```
2. Navigate to the project's root directory and run the following command
```
docker compose up
```
3. The web server is now running on localhost:8000
4. Use a tool such a Postman to interact with the api
5. Visit https://github.com/drew18moore/flashcards-client for instructions on how to install and run the front-end
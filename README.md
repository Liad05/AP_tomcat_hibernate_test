# Scrabble Game

This is a Java-based Scrabble game developed with an MVVM (Model-View-ViewModel) architectural pattern. It follows the original rules of Scrabble, where players take turns forming words on a 15x15 game board. The game supports multi-platform gameplay, allowing for cross-play between PC and Android devices.

![Game Board](src/view/BackGround.png)

## Showcase
[Watch the game in action](https://youtu.be/z1_ss3ksOLg)

## Built With

- [Java](https://www.oracle.com/java/)
- [JavaFX](https://openjfx.io/)
- [Tomcat](http://tomcat.apache.org/)
- [MySQL](https://www.mysql.com/)

## Features

- **MVVM Structure**: The project follows the MVVM architectural pattern, promoting separation of concerns and maintainability of the codebase.

- **Client-Server Connectivity**: The game supports multiplayer functionality through client-server connectivity.

- **HTTP Requests and Tomcat Servlet**: Communication between client and server is facilitated using HTTP requests (POST and GET) and a Tomcat Servlet.

- **MySQL Server Integration**: The game state can be saved and loaded at a later date through a MySQL server.

- **Cross-Platform Play**: A companion [Android app](https://github.com/Liad05/Scrabbler_game_android_app) built on the same logic and functionality as the PC version, supports cross-platform play.

## Prerequisites

- Java Development Kit (JDK) version 19 or higher
- Android device (for the Android app)

## Installation
git clone https://github.com/roeimichael/ScrabbleGame.git

## Usage
Launch the Scrabble game application and connect to the server by entering the server IP address and port. Once connected, you can form words on the game board using your letter tiles. The game ends when all letter tiles are used or when no more valid words can be formed, and the player with the highest score wins.

## Contributors
- **Roei Michael**
- **Liad Gam**
- **Alon Raicher**
- **Asaf Koren**
## Contact
If you have any questions, suggestions, or feedback, please feel free to contact us!

Happy Scrabble gaming!

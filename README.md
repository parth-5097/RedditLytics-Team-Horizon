# RedditLytics

Redditlytics is a Java-based project leveraging Akka, Akka Test, and Akka actors to perform synchronous extraction of Reddit posts and conduct sentiment analysis on each post individually. The project architecture is centered around Java and incorporates JUnit for testing and JavaScript for specific functionalities.

## Overview

The primary functionality of Redditlytics involves:

- **Synchronous Post Retrieval**: Utilizing Akka actors, the system synchronously extracts Reddit posts.
- **Sentiment Analysis**: Conducts sentiment analysis on each post retrieved, providing individualized results.
- **Technology Stack**: Primarily built using Java and incorporates Akka framework extensively. Additionally, Akka Test ensures robustness, while JUnit facilitates comprehensive testing. JavaScript is used for specific features.

## Components

### 1. Akka Actors
The project structure heavily relies on Akka actors to manage synchronous post extraction and analysis.

### 2. Sentiment Analysis
The core functionality involves sentiment analysis executed in real-time on each Reddit post.

### 3. Testing with Akka Test and JUnit
Robustness is ensured through extensive testing using Akka Test and JUnit, ensuring the reliability of the system.

## Usage
To run Redditlytics, follow these steps:

1. Ensure Java is installed on your system.
2. Install the necessary dependencies, primarily Akka and JUnit.
3. Execute the main Java file to initiate the process.

## Contributing
Contributions are welcome! Feel free to open issues or submit pull requests.

## License
This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).

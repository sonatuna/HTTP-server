# HTTP/1.1-Compliant Server in Java
A simple HTTP server implemented from scratch using Java Sockets.
# Features So Far
- Multithreaded request handling
- Manually parses HTTP requests using InputStreams
- Supports basic HTTP methods (GET, HEAD, POST)
- Serves static files from /resources directory
- Returns appropriate MIME typed response headers for requested resources
- Timestamped logs showing request origin, method, response code, and timing
- Uses the Strategy design pattern to handle different HTTP methods
- Gracefully handles I/O errors, malformed requests, and runtime exceptions with appropriate HTTP status codes (400, 500)
# Next Up
- Add support for more HTTP methods
- Advanced header support
- Security features
# Demo
### GET request
server<br>
<img width="551" alt="Screen Shot 2025-07-01 at 5 51 52 AM" src="https://github.com/user-attachments/assets/5aa4a33b-71c1-4279-ba1d-a7e8060e1242" /><br>
client (curl)<br>
<img width="616" alt="Screen Shot 2025-06-26 at 10 29 28 PM" src="https://github.com/user-attachments/assets/57e21a03-82e9-40a2-b4e6-ddad9c0acce8" />
### POST request
<img width="681" alt="Screen Shot 2025-07-01 at 5 53 16 AM" src="https://github.com/user-attachments/assets/605aa905-271a-416e-9b0c-a7aba117376f" /><br>
<img width="501" alt="Screen Shot 2025-06-26 at 10 46 01 PM" src="https://github.com/user-attachments/assets/0c3578d9-33fa-42e7-b510-88a9c419eb3e" />

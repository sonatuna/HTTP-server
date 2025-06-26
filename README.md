# HTTP/1.1-Compliant Server in Java
A simple HTTP server implemented from scratch using Java Sockets.
# Features So Far
- Manually parses HTTP requests using InputStreams
- Supports basic HTTP methods (GET, HEAD, POST)
- Serves static files from /resources directory
- Returns appropriate MIME typed response headers for requested resources
- Timestamped logs showing request origin, method, response code, and timing
- Uses the Strategy design pattern to handle different HTTP methods
# Next Up
- Add support for more HTTP methods
- Keep-Alive connections and chunked transfer encoding 
- Support concurrent client connections using threads
- Advanced header support
- More robust logging and request validation
- Security features
# Demo
### GET request
server<br>
<img width="550" alt="Screen Shot 2025-06-26 at 10 29 03 PM" src="https://github.com/user-attachments/assets/18315630-7ae7-4604-bca2-23541487ad11" /><br>
client (curl)<br>
<img width="616" alt="Screen Shot 2025-06-26 at 10 29 28 PM" src="https://github.com/user-attachments/assets/57e21a03-82e9-40a2-b4e6-ddad9c0acce8" />
### POST request
<img width="553" alt="Screen Shot 2025-06-26 at 10 43 14 PM" src="https://github.com/user-attachments/assets/a6358431-69e8-46fc-a6d1-fafb943f3e30" /><br>
<img width="501" alt="Screen Shot 2025-06-26 at 10 46 01 PM" src="https://github.com/user-attachments/assets/0c3578d9-33fa-42e7-b510-88a9c419eb3e" />

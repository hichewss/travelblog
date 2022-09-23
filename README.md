# Travel Blog Application
Included is a multi-activity travel blog application created within Android Studio using Kotlin and XML. The user first interacts with a login screen
(username and password are both "user") and is redirected to the main activity page featuring a preview list of published travel blogs. If a blog is 
selected, the user is then led to a new page with the expanded details of that specific blog. Blog data is derived from the internet using an HTTP client class
and respective JSON files. Should the user return to the home screen or a different application without completely exiting from the travel blog application, 
their login will maintain, and they will resume from the last screen they were on when they exited. The list of blogs can additionally be sorted by date or title, or directly searched using the toolbar.


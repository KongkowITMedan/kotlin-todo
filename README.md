# Kotlin Todo Backend Web API

Aplikasi sederhana Web API menggunakan sparkjava.

Cara menjalankan:
1. clone repository ini
2. build project (dari terminal):
    `./gradlew build`
3. jalankan aplikasi (dari terminal):
    `java -jar build/libs/kotlin-todo-1.0-SNAPSHOT-all.jar`
    
## API Endpoint
1. `GET /api/task`

    mengembalikan seluruh task (yang aktif dan completed)

2. `GET /api/task/active`

   mengembalikan seluruh task active

3. `GET /api/task/completed`

   mengembalikan seluruh task completed
   
   
4. `POST /api/task`
   
   membuat task baru, body request diisikan dengan json task baru.
   {"content": "", "editable": false, "completed": false}
   
5. `GET /api/task/:id`

    mengembalikan task :id {"content": "", "editable": false, "completed": false}
    
6. `PUT /api/task/:id`

    update task :id, request harus mengirimkan json lengkap 
    {"content": "", "editable": false, "completed": false}
   
7. `DELETE /api/task/:id`

    hapus task :id

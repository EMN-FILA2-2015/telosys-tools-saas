// Create a default user to be used until SecurityUtils is built
// use db_general
db.users.insert({"_id": "user_default", "email": "default@mail.com", "password": "default"});

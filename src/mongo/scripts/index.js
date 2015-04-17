// Ensure that for a given owner, there is only one project with this name
db.projects.createIndex({"owner":1,"name":1},{unique:true})
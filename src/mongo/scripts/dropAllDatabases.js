/**
 * Created by Adrian on 20/04/15.
 */
db.adminCommand("listDatabases").databases.forEach(
    function(d) { if (d.name !="local" && d.name != "admin") {
        print("Dropping database : " + d.name);
        db.getSiblingDB(d.name).dropDatabase();
    }
})

package com.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.inc;

@ApplicationScoped
public class PersonRepository {

    private final MongoClient mongoClient;
    private final MongoCollection<PersonEntity> coll;

    public PersonRepository(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
        this.coll = mongoClient.getDatabase("test").getCollection("persons", PersonEntity.class);
    }

    public String add(PersonEntity person) {
        return coll.insertOne(person).getInsertedId().asObjectId().getValue().toHexString();
    }

    public List<PersonEntity> getPersons() {
        return coll.find().into(new ArrayList<>());
    }

    public long anniversaryPerson(String id) {
        Bson filter = eq("_id", new ObjectId(id));
        Bson update = inc("age", 1);
        return coll.updateOne(filter, update).getModifiedCount();
    }

    public long deletePerson(String id) {
        Bson filter = eq("_id", new ObjectId(id));
        return coll.deleteOne(filter).getDeletedCount();
    }
}

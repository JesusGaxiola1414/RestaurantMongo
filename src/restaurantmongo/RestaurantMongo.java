/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restaurantmongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Aggregates.match;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author McRayo
 */
public class RestaurantMongo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MongoClient mongo = new MongoClient("localhost");
        MongoDatabase database = mongo.getDatabase("test");
        
// Crear collection a trabajar:
          MongoCollection<Document> collection = database.getCollection("restaurants");
      
// Se crean y insertan 3 Documentos  en restaurant collection , cada documento es un restaurant 
// con nombre , estrellas y categorias almscenados como una matriz:
          List documents = asList(
          new Document("name","Italianis")
          .append("stars",4)
          .append("Categorias", asList("Pizza","Italiana","Tacos")), 
           new Document("name","Chicago")
          .append("Stars",3)
          .append("categoria", asList("Pizza","Hamburgesas","Alitas")),
           new Document("name","Sushilito")
          .append("stars",2)
          .append("Categorias", asList("Sushi","Boneles","Postres")));
           collection.insertMany(documents);
          
// Se ejecuta una consulta para tener los documentos de restaurant collection y se guarda como matriz:
          List results = collection.find().into(new ArrayList<>());
     
// Se crea un indice en el campo de nombre con orden de clasificacion ascendente:
          collection.createIndex(Indexes.ascending("name"));
 
// Se obtienen los documentos de la coleccion restaurants que tengan la categoria de Pizza: 
          collection.aggregate(asList(match(eq("categoria","Pizza"))));
//        mongoClient.close();

// Se obtienen los documentos de la coleccion restaurants que tengan en su nombre la palabra Sushi:
          collection.aggregate(asList(match(eq("name","Sushi"))));
//        mongoClient.close();

// Elimina un documento buscandolo por su identificador:
          DeleteResult deleteResult = collection.deleteOne(Filters.eq("name","Sushilito"));
    
// Se obtiene los restaurantes con mas de 4 estrellas
// eq es para que devuelva los que tengan una estrella exactamente 
// gt es para los que tengan mas de una:
         Document timespan = new Document();
         timespan.append("$gt",4);
         timespan.append("$eq",4);
         Document condition = new Document("stars",timespan);
        
// Elimina los documentos buscados cuyas estresllas sean menor o igual a 3:     
         DeleteResult deleteResultt = collection.deleteMany(Filters.lte("stars",3));
   
// Agrega una nueva categoria a Sushilito:       
         UpdateResult update;
         update= collection.updateOne(
                      Filters.eq("name", "Sushilito"),
                      Updates.addToSet("categoria", "China"));

// Saca la lista de todos los restaurantes:     
        for(Document list : collection.find()){
            System.out.println(list.toJson());
        }
    }
    
}

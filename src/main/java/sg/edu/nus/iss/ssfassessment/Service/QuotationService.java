package sg.edu.nus.iss.ssfassessment.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;


import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.ssfassessment.Model.Quotation;

@Service
public class QuotationService {

    private static final String URL = "https://quotation.chuklee.com";

    public Optional<Quotation> getQuotations(List<String> items){

        JsonArrayBuilder jsonABuilder = Json.createArrayBuilder();
        for (int i = 0 ; i < items.size(); i ++){
            jsonABuilder.add(items.get(i));
        }
        JsonArray jsonA = jsonABuilder.build();
       
        String quotationUrl = UriComponentsBuilder
        .fromUriString(URL)
        .path("/quotation")
        .toUriString();

        RequestEntity<String> req = RequestEntity
        .post(quotationUrl)
        .contentType(MediaType.APPLICATION_JSON)
        .header("Accept", "MediaType.APPLICATION_JSON")
        .body(jsonA.toString(), String.class);
        

        RestTemplate rTemplate = new RestTemplate();
        ResponseEntity<String> resp = rTemplate.exchange(req, String.class);
        Quotation quotations = new Quotation();

        try (InputStream is = new ByteArrayInputStream(resp.getBody().getBytes())){
            JsonReader r = Json.createReader(is);
            JsonObject o = r.readObject();
            JsonArray quotationsJsonArray = o.getJsonArray("quotations");
            System.out.println(">>>>>>>>> quotation from website" + o);  
        

           
            for (int i = 0 ; i < quotationsJsonArray.size(); i ++){
                JsonObject obj = quotationsJsonArray.getJsonObject(i);
                System.out.println(obj);
                quotations.addQuotation(obj.get("item").toString(), Float.parseFloat(obj.get("unitPrice").toString()));
            }
            

            quotations.setQuoteId(o.getString("quoteId"));
            
            return Optional.of(quotations);

        }catch (Exception ex) {
            System.err.printf(">>>> Error: %s\n", ex.getMessage());
            ex.printStackTrace();
        }
        
        return Optional.empty();
        
        
    }

    
}

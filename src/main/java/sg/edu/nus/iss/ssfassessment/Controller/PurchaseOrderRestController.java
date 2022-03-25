package sg.edu.nus.iss.ssfassessment.Controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.ssfassessment.Model.Quotation;
import sg.edu.nus.iss.ssfassessment.Service.QuotationService;

@RestController
@RequestMapping(path="/api")
public class PurchaseOrderRestController {

    @Autowired
    private QuotationService qService;

    @PostMapping(path="/po")
    public ResponseEntity<String> sendPurchaseOrder(

        @RequestBody String payload){
        System.out.printf(">>>>>payload: %s\n", payload);

        try (InputStream is = new ByteArrayInputStream(payload.getBytes())) {
            JsonReader r = Json.createReader(is);
            JsonObject req = r.readObject();
            JsonArray jsonArray = req.getJsonArray("lineItems");
            
            List<String> cart = new ArrayList<>();
            for (int i = 0 ; i < jsonArray.size(); i++){
                JsonObject item = jsonArray.getJsonObject(i);
                cart.add(item.getString("item"));
            }
            System.out.println(">>>>>>>>>>cart items: "+cart);
            
            Quotation quotations = qService.getQuotations(cart).get();
            System.out.println(">>>>>>>>> getquotation successful:" + quotations.toString());  

            String name = req.getString("name");
            System.out.println(">>>>>> Name: " + name);
            String invoiceId = quotations.getQuoteId();
            System.out.println(">>>>>> invoiceId: " + invoiceId);
            Double total = 0.0;
            for(int i = 0; i < quotations.getQuotations().size() ; i++){
                JsonObject item = jsonArray.getJsonObject(i);
                total = total + (item.getInt("quantity") * quotations.getQuotation(item.get("item").toString()));
            }
            
            System.out.println(">>>>TOTAL:" +total);
            JsonObject jsonO = Json.createObjectBuilder()
            .add("invoiceId", invoiceId)
            .add("name", name)
            .add("total", total)
            .build();
            System.out.print(">>>>>>>JSON OBJ: " + jsonO.toString());
           return ResponseEntity.ok(jsonO.toString());

        }catch (Exception ex) {
            JsonObject result = Json.createObjectBuilder()
                .add("error", ex.getMessage())
                .build();
            return ResponseEntity.status(400).body(result.toString());
        } 

        

        
        }

    
}

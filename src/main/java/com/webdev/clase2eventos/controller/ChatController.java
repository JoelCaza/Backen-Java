package com.webdev.clase2eventos.controller;


import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import com.webdev.clase2eventos.model.Producto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.webdev.clase2eventos.repository.ProductoRepostiroy;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/chat")
@CrossOrigin(origins = "http://localhost:5173")
    public class ChatController {

        private final ProductoRepostiroy productoRepostiroy;

        public ChatController(ProductoRepostiroy productoRepostiroy){
            this.productoRepostiroy = productoRepostiroy;

        }


        @Value("${gemini.api.key}")
        private String apiKey;

        @PostMapping
        public ResponseEntity<Map<String,String>>  preguntar(@RequestBody Map<String,String> body){
            String mensaje = body.get("mensaje");
            if (mensaje==null || mensaje.trim().isEmpty()){
                return ResponseEntity.badRequest().body(Map.of("error","El mensaje es requerido"));
            }

            try{
                Client client = Client.builder().apiKey(apiKey).build();
                List<Producto> productos = productoRepostiroy.findAll();

                StringBuilder catalogo = new StringBuilder("Catalogo acutal de productos en stock :\n");
                if (productos.isEmpty()){
                    catalogo.append("Acutualmente no hay productos dispoibles");
                }else {
                    for (Producto p : productos){
                        catalogo.append("- ").append(p.getNombre())
                                .append("(Precio:$").append(p.getPrecio()).append("(Stock:").append(p.getStock()).append(")\n");
                    }
                }

                String reglasDelSistema =
                        "Eres un asisten virutal de un Ecommerce se amable y cordial"+
                        "Regla 1: Solo puedes hablar de tecnolgia y si te pregutan sobre ENVIOS, responde que tenemos envios a todo el pasi por $5 dolares adicionale"+
                        "Regla 2: Si te preguntan solo tenemos pagos en trasnferencia y tarjes de credito, las transferencias aceptamos solo de bancos como:Pichincha,Produbanco"
                        +"Regla 3 : Si el usuario te preguta cualquier otra cosa fuera del ecommerce o de las reglas responde que no puedes ayudarle en eso"+
                                "Regla 4:Basa tus respuestas de inventario EXCLUSIVAMENTE en el siguiente catalogo:\n"+
                        catalogo.toString();

                GenerateContentConfig config = GenerateContentConfig.builder()
                        .systemInstruction(Content.builder()
                                .parts(List.of(Part.builder().text(reglasDelSistema).build()))
                                .build())
                        .temperature(0.3f)
                        .build();

                GenerateContentResponse response = client.models.generateContent(
                        "gemini-3.5-flash",
                        mensaje,
                        config
                );
                return ResponseEntity.ok(Map.of("respuesta",response.text()));
            } catch (Exception e) {
                return ResponseEntity.internalServerError()
                        .body(Map.of("error","Hubo un problema con la IA"+e.getMessage()));

            }
        }

    }

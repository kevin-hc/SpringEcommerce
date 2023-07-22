package com.example.ecommerce.controller;

import com.mercadopago.MercadoPago;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.BackUrls;
import com.mercadopago.resources.datastructures.preference.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.ecommerce.model.Carrito;

@Controller
@RequestMapping("/crear-orden")
public class OrdenController {

    @Value("${mercadopago.access_token}")
    private String accessToken;

    @PostMapping
    public void crearOrden(HttpServletResponse response, @ModelAttribute Carrito carrito) throws IOException {
        try {
            // Configurar el token de acceso de Mercado Pago
            MercadoPago.SDK.setAccessToken(accessToken);

            // Crear una preferencia de pago en Mercado Pago
            Preference preference = new Preference();

            // Obtener los detalles de los productos en el carrito y agregarlos a la preferencia
            List<Item> items = new ArrayList<>();

            Item item = new Item();
            item.setTitle(carrito.getProducto().getNombre());
            item.setQuantity(carrito.getCantidad());
            item.setUnitPrice((float) carrito.getProducto().getPrecio());
            items.add(item);

            preference.setItems((ArrayList<Item>) items);

            // También puedes configurar las URLs de retorno:
            preference.setBackUrls(
                    new BackUrls()
                            .setSuccess("http://localhost:8080/pagoRealizado")
                            .setFailure("http://localhost:8080/pagoFallido")
                            .setPending("http://localhost:8080/pagoPendiente")
            );

            // Guardar la preferencia en Mercado Pago
            preference.save();

            if (preference.getId() != null) {
                // Obtener la URL de pago generada por Mercado Pago
                String urlPago = preference.getInitPoint();

                // Redirigir al usuario a la URL de pago
                response.sendRedirect(urlPago);
            } else {
                throw new MPException("Error al crear la preferencia de pago");
            }
        } catch (MPException e) {
            // Manejar la excepción en caso de error en la comunicación con Mercado Pago
            e.printStackTrace();
            response.getWriter().write("Error al crear la orden en Mercado Pago");
        }
    }
}

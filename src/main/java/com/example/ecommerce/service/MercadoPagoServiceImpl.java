package com.example.ecommerce.service;



import com.example.ecommerce.model.Carrito;
import com.mercadopago.*;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.BackUrls;
import com.mercadopago.resources.datastructures.preference.Item;
import com.mercadopago.resources.datastructures.preference.Payer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("mercadoPagoService")
@Transactional
public class MercadoPagoServiceImpl implements MercadoPagoService {

    @Override
    public Preference procesarPagoDePedido(String[] items, Integer[] cantidades, Float[] precios) throws MPException {
        MercadoPago.SDK.configure("TEST-867780493823573-112218-33ab1227d9d3f3d6db2332eb892bd369-200165530");

        Payer payer = new Payer();
        Preference preference = new Preference();

        preference.setPayer(payer);

        preference.setBackUrls(
                new BackUrls().setFailure("http://localhost:8080/pagoFallido")
                        .setPending("http://localhost:8080/pagoPendiente")
                        .setSuccess("http://localhost:8080/pagoRealizado")
        );

        for (int i = 0; i < items.length; i++) {
            Item item = new Item();
            item.setTitle(items[i]);

            for (int j = 0; j < cantidades.length; j++) {
                if (i == j) {
                    item.setQuantity(cantidades[j]);
                    break;
                }
            }

            for (int k = 0; k < precios.length; k++) {
                if (i == k) {
                    item.setUnitPrice(precios[k]);
                    break;
                }
            }

            preference.appendItem(item);
        }


        return preference.save();
    }

    @Override
    public Preference createPreference(Carrito carrito) {
        return null;
    }

}

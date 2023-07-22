package com.example.ecommerce.service;

import com.example.ecommerce.model.Carrito;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Preference;

public interface MercadoPagoService {

    public Preference procesarPagoDePedido(String[] items, Integer[] cantidades, Float[] precios) throws MPException;

    Preference createPreference(Carrito carrito);
}

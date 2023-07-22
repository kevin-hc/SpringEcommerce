package com.example.ecommerce.controller;

import com.example.ecommerce.model.Carrito;
import com.example.ecommerce.service.MercadoPagoService;
import com.mercadopago.MercadoPago;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Preference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/payment")
public class MercadoPagoController {

    private final MercadoPagoService mercadoPagoService;

    @Autowired
    public MercadoPagoController(MercadoPagoService mercadoPagoService) {
        this.mercadoPagoService = mercadoPagoService;
    }

    @GetMapping("/create")
    public ModelAndView createPreference() throws MPException {
        ModelAndView modelAndView = new ModelAndView();
        Carrito carrito = new Carrito(); // Crea una instancia de Carrito si es necesario
        Preference preference = mercadoPagoService.createPreference(carrito);
        if (preference == null) {
            modelAndView.setViewName("error");
            modelAndView.addObject("errorMessage", "Error al crear la preferencia de pago");
            return modelAndView;
        }
        String preferenceId = preference.getId();
        String redirectUrl = "https://www.mercadopago.com/checkout?preference_id=" + preferenceId;
        modelAndView.setViewName("redirect:" + redirectUrl);
        return modelAndView;
    }
}

package com.example.json_ex.configuration;

import com.example.json_ex.model.dto.ex4.UserFirstLastAgeSoldProductsDto;
import com.example.json_ex.model.entity.Product;
import com.example.json_ex.model.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class ApplicationBeanConfiguration {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

//        Converter<User, Integer> toSoldProductsCount =
//                ctx -> Math.toIntExact(ctx.getSource() == null || ctx.getSource().getSoldProducts() == null ? 0L :
//                        ctx.getSource().getSoldProducts().size());

        Converter<Set<Product>, Integer> toProductCount =
                ctx -> Math.toIntExact(ctx.getSource() == null ? 0L :
                        ctx.getSource().size());

        modelMapper.createTypeMap(User.class, UserFirstLastAgeSoldProductsDto.class)
                .addMappings(mpr -> mpr.using(toProductCount).<Integer>map(User::getSoldProducts,
                        (dest, v) -> dest.getSoldProducts().setCount(v)));

        return modelMapper ;
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }
}

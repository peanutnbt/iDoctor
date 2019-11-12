package com.example.idoctor.mapper;

public interface IMapper<From, To> {

    To map(From from);
}

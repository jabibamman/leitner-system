package com.esgi.leitnersystem.cucumber;

import com.esgi.leitnersystem.LeitnerSystemApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = LeitnerSystemApplication.class)
public class CucumberSpringConfiguration {}

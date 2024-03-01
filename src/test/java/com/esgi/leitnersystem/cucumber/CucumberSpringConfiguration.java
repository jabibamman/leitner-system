package com.esgi.leitnersystem.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import com.esgi.leitnersystem.LeitnerSystemApplication;

@CucumberContextConfiguration
@SpringBootTest(classes = LeitnerSystemApplication.class)
public class CucumberSpringConfiguration {
}

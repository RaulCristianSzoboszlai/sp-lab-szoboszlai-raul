package com.example.demo;

import com.example.demo.difexamples.ClientComponent;
import com.example.demo.difexamples.SingletonComponent;
import com.example.demo.difexamples.TransientComponent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		// SpringApplication.run(DemoApplication.class, args);

        // Preia controlul asupra contextului de injecție de dependențe
        ApplicationContext context = SpringApplication.run(DemoApplication.class, args);

        // Cere o instanță Transient. Spring creează una nouă de fiecare dată.
        TransientComponent transientBean = context.getBean(TransientComponent.class);
        transientBean.operation();

        transientBean = context.getBean(TransientComponent.class);
        transientBean.operation();

        // Cere o instanță Singleton. Spring va returna mereu aceeași instanță unică.
        SingletonComponent singletonBean = context.getBean(SingletonComponent.class);
        singletonBean.operation();

        singletonBean = context.getBean(SingletonComponent.class);
        singletonBean.operation();

        // Cere componenta Client care le folosește pe amândouă
        ClientComponent c = context.getBean(ClientComponent.class);
        c.operation();

        // Poți cere instanța și după numele ei (camelCase-ul numelui clasei)
        c = (ClientComponent) context.getBean("clientComponent");
        c.operation();
	}

}

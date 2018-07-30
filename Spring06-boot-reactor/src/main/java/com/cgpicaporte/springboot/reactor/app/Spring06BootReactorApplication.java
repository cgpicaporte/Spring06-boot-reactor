package com.cgpicaporte.springboot.reactor.app;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cgpicaporte.springboot.reactor.app.models.Usuario;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class Spring06BootReactorApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(Spring06BootReactorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(Spring06BootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// FORMA 1: solo hacemos una cosa
		/*
		 * Flux<String> nombres = Flux.just("Andres","Pedro","Diego","Juan")
		 * .doOnNext(elemento -> System.out.println(elemento));
		 */

		// FORMA 2: hacemos varias cosas, añadiendo llaves
		/*
		 * Flux<String> nombres = Flux.just("Andres","Pedro","Diego","Juan")
		 * .doOnNext(elemento -> { System.out.println(new Date().getTime() + " " +
		 * elemento); System.out.println("hacemos varias cosas"); } );
		 */

		// FORMA 3: reducimos más, aplicando con :: el metodo al elemento
		Flux<String> nombres = Flux.just("Andres", "Pedro", "Diego", "Juan").doOnNext(System.out::println);

		// Nos subcribimos
		// nombres.subscribe();

		// También se ejecuta aquí el logger.
		nombres.subscribe(log::info); // que equivale a nombres.subscribe(e -> log.info(e));

		// FORMA 4: emulamos un error
		Flux<String> nombresEmulaError = Flux.just("Andrés", "Pedro", "", "Diego", "Juan").doOnNext(e -> {
			if (e.isEmpty()) {
				throw new RuntimeException("Nombres no pueden ser vacíos");
			}

			System.out.println(e);

		}// fin llave para ejecutar varias sentencias de la funcion fecha
		);
		nombresEmulaError.subscribe(e -> log.info(e), error -> log.error(error.getMessage()), new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				log.info("Ha finalizado la ejecución del observable con éxito!");
			}
		});

		// FORMA 5: se ejecuta todo y una sentencia final cuando se ha completado el
		// observable
		log.info("************************************************************************************");
		log.info("FORMA 5: se ejecuta todo y una sentencia final cuando se ha completado el observable");
		log.info("************************************************************************************");

		Flux<String> nombresEjecutaTodo = Flux.just("Andrés", "Pedro", "Carlos", "Diego", "Juan").doOnNext(e -> {
			if (e.isEmpty()) {
				throw new RuntimeException("Nombres no pueden ser vacíos");
			}

			System.out.println(e);

		}// fin llave para ejecutar varias sentencias de la funcion fecha
		).map(nombre -> {
			return nombre.toUpperCase();
		});

		nombresEjecutaTodo.subscribe(e -> log.info(e), error -> log.error(error.getMessage()), new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				log.info("");
				log.info("Ha finalizado la ejecución del observable con éxito!");
				log.info("************************************************************************************");
				log.info("suscribe tiene 3 parámetros: suscribe(ejecución normal, si error, si se completa)");
				log.info("************************************************************************************");
			}
		});

		// FORMA 6: map
		log.info("************************************************************************************");
		log.info("FORMA 6: map");
		log.info("************************************************************************************");

		// Pasamos de Flux<String> a Flux<Usuario>
		Flux<Usuario> nombresUsoMap = Flux.just("Andrés", "Pedro", "Carlos", "Diego", "Juan")
				.map(nombre -> new Usuario(nombre.toUpperCase(), null)).doOnNext(usuario -> {
					if (usuario == null) {
						throw new RuntimeException("Nombres no pueden ser vacíos");
					}

					System.out.println(usuario.getNombre());

				}// fin llave para ejecutar varias sentencias de la funcion fecha
				).map(usuario -> {
					String nombre = usuario.getNombre().toLowerCase();
					usuario.setNombre(nombre);
					return usuario; // al devolver usuario he de cambiar el Flux<String> a Flux<Usuario>
				});

		nombresUsoMap.subscribe(e -> log.info(e.toString()), error -> log.error(error.getMessage()), new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				log.info("");
				log.info("Ha finalizado la ejecución del observable con éxito!");
				log.info("************************************************************************************");
			}
		});

		// FORMA 7: filter
		log.info("************************************************************************************");
		log.info("FORMA 7: filter");
		log.info("************************************************************************************");

		// Pasamos de Flux<String> a Flux<Usuario>
		Flux<Usuario> nombresUsoFilter = Flux
				.just("Andrés Guzmán", "Pedro Fulano", "Carlos Picaporte", "Diego Sultano", "Juan Mengano", "Bruce Lee",
						"Bruce Willis")
				.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.filter(usuario -> usuario.getNombre().equalsIgnoreCase("bruce")).doOnNext(usuario -> {
					if (usuario == null) {
						throw new RuntimeException("Nombres no pueden ser vacíos");
					}

					System.out.println(usuario.getNombre() + " " + usuario.getApellido());

				}// fin llave para ejecutar varias sentencias de la funcion fecha
				).map(usuario -> {
					String nombre = usuario.getNombre().toLowerCase();
					String apellido = usuario.getApellido().toLowerCase();
					usuario.setNombre(nombre);
					usuario.setApellido(apellido);
					return usuario; // al devolver usuario he de cambiar el Flux<String> a Flux<Usuario>
				});

		nombresUsoFilter.subscribe(e -> log.info(e.toString()), error -> log.error(error.getMessage()), new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				log.info("");
				log.info("Ha finalizado la ejecución del observable con éxito!");
				log.info("************************************************************************************");
			}
		});

		// FORMA 8: inmutable
		log.info("************************************************************************************");
		log.info("FORMA 8: inmutable");
		log.info("************************************************************************************");

		// Pasamos de Flux<String> a Flux<Usuario>
		Flux<String> nombresInicial = Flux.just("Andrés Guzmán", "Pedro Fulano", "Carlos Picaporte", "Diego Sultano",
				"Juan Mengano", "Bruce Lee", "Bruce Willis");

		Flux<Usuario> nombresInmutable = nombresInicial
				.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.filter(usuario -> usuario.getNombre().equalsIgnoreCase("bruce")).doOnNext(usuario -> {
					if (usuario == null) {
						throw new RuntimeException("Nombres no pueden ser vacíos");
					}

					System.out.println(usuario.getNombre() + " " + usuario.getApellido());

				}// fin llave para ejecutar varias sentencias de la funcion fecha
				).map(usuario -> {
					String nombre = usuario.getNombre().toLowerCase();
					String apellido = usuario.getApellido().toLowerCase();
					usuario.setNombre(nombre);
					usuario.setApellido(apellido);
					return usuario; // al devolver usuario he de cambiar el Flux<String> a Flux<Usuario>
				});

		nombresInmutable.subscribe(e -> log.info(e.toString()), error -> log.error(error.getMessage()), new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				log.info("");
				log.info("Ha finalizado la ejecución del observable con éxito!");
				log.info("************************************************************************************");
			}
		});

		// FORMA 9: Creando un Flux (Observable) a partir de un List o Iterable
		log.info("************************************************************************************");
		log.info("FORMA 9: Creando un Flux (Observable) a partir de un List o Iterable");
		log.info("************************************************************************************");

		List<String> usuariosList =  new ArrayList<>();
		usuariosList.add("Andrés Guzmán");
		usuariosList.add("Pedro Fulano");
		usuariosList.add("Carlos Picaporte");
		usuariosList.add("Diego Sultano");
		usuariosList.add("Juan Mengano");
		usuariosList.add("Bruce Lee");
		usuariosList.add("Bruce Willis");

		
		//Flux<String> nombresList = Flux.just("Andrés Guzmán", "Pedro Fulano", "Carlos Picaporte", "Diego Sultano","Juan Mengano", "Bruce Lee", "Bruce Willis");
		Flux<String> nombresList = Flux.fromIterable(usuariosList);

		Flux<Usuario> usuarios = nombresList
				.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.filter(usuario -> usuario.getNombre().equalsIgnoreCase("bruce")).doOnNext(usuario -> {
					if (usuario == null) {
						throw new RuntimeException("Nombres no pueden ser vacíos");
					}

					System.out.println(usuario.getNombre() + " " + usuario.getApellido());

				}// fin llave para ejecutar varias sentencias de la funcion fecha
				).map(usuario -> {
					String nombre = usuario.getNombre().toLowerCase();
					String apellido = usuario.getApellido().toLowerCase();
					usuario.setNombre(nombre);
					usuario.setApellido(apellido);
					return usuario; // al devolver usuario he de cambiar el Flux<String> a Flux<Usuario>
				});

		usuarios.subscribe(e -> log.info(e.toString()), error -> log.error(error.getMessage()), new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				log.info("");
				log.info("Ha finalizado la ejecución del observable con éxito!");
				log.info("************************************************************************************");
			}
		});
		
		
	}
}

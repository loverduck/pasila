//package com.ssafy.self.demo;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StopWatch;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.sql.SQLOutput;
//import java.util.Arrays;
//
//@SpringBootApplication
//@Component
//public class DemoApplication {
//
//	@Autowired
////	RestTemplateBuilder restTemplateBuilder;
//	WebClient.Builder webClientBuild;
//
//	public static void main(String[] args) {
//		SpringApplication.run(DemoApplication.class, args);
//	}
//
//	@Bean
//	public ApplicationRunner applicationRunner(){
//
//	return args -> {
//			StopWatch stopWatch = new StopWatch();
//			stopWatch.start();
//
//			WebClient webClient = webClientBuild.baseUrl("https://api.github.com").build();
//			Flux<GithubRepository> repos = webClient.get().uri("/users/youkyoungJung/repos")
//							.retrieve()
//							.bodyToFlux(GithubRepository.class);
//			Flux<GitCommitRepository> url = webClient.get().uri("/repos/youkyoungJung/solved_baekjoon/commits")
//							.retrieve()
//							.bodyToFlux(GitCommitRepository.class);
//			repos.doOnNext(ra ->{
////					System.out.println("repos: " +ra.getUrl());
//			}).subscribe();
//
//			url.doOnNext(ca->{
////					System.out.println("msg: " + ca.getUrl());
//			}).subscribe();
//
//			stopWatch.stop();
//			System.out.println(stopWatch.prettyPrint());
//		};
//	}
//}
//
////1.
////			RestTemplate restTemplate = restTemplateBuilder.build();
////			String result = restTemplate.getForObject("https://api.github.com/users/youkyoungJung/repos", String.class);
////			System.out.println(result);
//
////2.
////			StopWatch stopWatch = new StopWatch();
////			stopWatch.start();
////
////			RestTemplate restTemplate = restTemplateBuilder.build();
////			GithubRepository[] result = restTemplate.getForObject("https://api.github.com/users/youkyoungJung/repos", GithubRepository[].class);
////            assert result != null;
////            Arrays.stream(result).forEach(r -> {
////				System.out.println("repo: " + r.getName());
////			});
////
////			GithubRepository[] result2 = restTemplate.getForObject("https://api.github.com/users/youkyoungJung/repos", GithubRepository[].class);
////			assert result2 != null;
////			Arrays.stream(result2).forEach(r -> {
////				System.out.println("id: " + r.getId());
////			});

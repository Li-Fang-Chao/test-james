package james.li.concurrencyinpractice;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * 
 * @author jamli
 *
 */
public class Task006 {

	public static void main(String[] args) throws InterruptedException {
		
		TestHarness tester = new TestHarness();
		
		System.out.println(tester.timeTasks(2, getNBAGamePlayers(), 10));
		
		System.out.println(tester.timeTasks(2, getGoogleHomePage(), 10));
	
	}

	private static Runnable getNBAGamePlayers() {
		
		
		Runnable getNBAPlayers =() -> {
			
			System.out.println("Getting all NBA players from rapidapi");
			
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("https://free-nba.p.rapidapi.com/players?page=0&per_page=300"))
					.header("X-RapidAPI-Key", "77f0442583msh830f5aef3be6049p153fe0jsnd0b59859ff4e")
					.header("X-RapidAPI-Host", "free-nba.p.rapidapi.com")
					.method("GET", HttpRequest.BodyPublishers.noBody())
					.build();
			HttpResponse<String> response;
			try {
				response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
				System.out.println(response.body());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		};
		return getNBAPlayers;
	}
	
	private static Runnable getGoogleHomePage() {
		
		System.out.println("Getting google index page");
		
		
		Runnable getGoogleHomePage =() -> {
			
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("https://google.com"))
					.method("GET", HttpRequest.BodyPublishers.noBody())
					.build();
			HttpResponse<String> response;
			try {
				response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
				System.out.println(response.body());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		};
		return getGoogleHomePage;
	}

}
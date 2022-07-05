package ru.job4j.weather;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;

@RestController
public class WeatherControl {

    private final WeatherService weatherService;

    public WeatherControl(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping(value = "/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Weather> all() {
        Flux<Weather> data = weatherService.all();
        Flux<Long> delay = Flux.interval(Duration.ofSeconds(1));
        return Flux.zip(data, delay).map(Tuple2::getT1);
    }

    @GetMapping(value = "/get/{id}")
    public Mono<Weather> get(@PathVariable Integer id) {
        return weatherService.findById(id);
    }

    @GetMapping("/hottest")
    public Mono<Weather> getHottest() {
        return weatherService.getHottest();
    }

    @GetMapping("/cityGreateThan/{temp}")
    public Flux<Weather> getGreaterThan(@PathVariable("temp") int temp) {
        return weatherService.getGreaterThan(temp);
    }
}

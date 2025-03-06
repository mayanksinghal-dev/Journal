package net.engineeringdigest.journalApp.health;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class HealthController {

    @GetMapping("check")
    public String testing(){
        return "oK";
    }
}

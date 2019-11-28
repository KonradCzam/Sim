import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NpcCreationException extends Exception {
    String message;
}

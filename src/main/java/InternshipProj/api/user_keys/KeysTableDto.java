package InternshipProj.api.user_keys;

public class KeysTableDto {
    private Long id;
    private String key;
    private Long userId;
    private String api;
    //Default Const.
    public KeysTableDto() {
    }

    public KeysTableDto(Long id, String key, Long userId, String api) {
        this.id = id;
        this.key = key;
        this.userId = userId;
        this.api = api;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }
}

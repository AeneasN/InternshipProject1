package InternshipProj.api.ips;

import InternshipProj.api.users.Userid;
import io.ipinfo.api.IPinfo;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IPService {
    @Autowired
    private IPRepository ipRepository;
    @Autowired
    private IPinfo ipinfo;


    public IPTable getIpLocation(Userid user, String ipAddress) {
        IPResponse response;
    try{
        response = ipinfo.lookupIP(ipAddress);
    }
    catch(RateLimitedException e){
            return null;
    }
    String city = response.getCity();
    String country = response.getCountryName();
    String region = response.getRegion();

    IPTable ipTable = new IPTable(user, ipAddress);
    ipTable.setCity(city);
    ipTable.setCountry(country);
    ipTable.setRegion(region);

    return ipRepository.save(ipTable);
    }
}
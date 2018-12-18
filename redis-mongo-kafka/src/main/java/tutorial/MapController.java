package tutorial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tutorial.dao.MapDao;

@RestController
@RequestMapping("/map")
public class MapController {

  @Autowired
  private MapDao mapDao;

  @RequestMapping(value = "/{key}", method = RequestMethod.PUT)
  public String put(@PathVariable("key") String key, @RequestParam final String val) {
    mapDao.put(key, val);
    return val;
  }

  @RequestMapping(value = "/{key}", method = RequestMethod.GET)
  public String get(@PathVariable("key") String key) {
    return mapDao.get(key);
  }

}

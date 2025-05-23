package domain_layer.transportationDomain;

import java.util.List;

public class ShipmentArea {

    private int id;
    private String name;
    private List<Site> sites;

    public ShipmentArea(int id, String name, List<Site> sites) {
        this.id = id;
        this.name = name;
        this.sites = sites;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Site> getSites() {
        return sites;
    }
    public void addSite(Site site) {
        // We assume there can't be two sites with the same name in the same shipment area
        for(Site s : sites) {
            if (s.getName().equals(site.getName())) {
                return;
            }
        }
        this.sites.add(site);
    }
    public void removeSite(Site site) {
        this.sites.remove(site);
    }

    public Site getSiteByName(String name) {
        for (Site site : sites) {
            if (site.getName().equals(name)) {
                return site;
            }
        }
        return null;
    }

    
    
}

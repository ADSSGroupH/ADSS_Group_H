package dal.transportation_dal;

import dto_folder.SiteDTO;

public interface  SiteDAO {
    public SiteDTO findSite(String name, int shipmentAreaId);
}

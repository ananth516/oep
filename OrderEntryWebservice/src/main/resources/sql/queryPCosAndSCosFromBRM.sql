select  cri.unique_id, cri.type
from config_offer_cos_map_t cocm, config_resource_info_t cri
where cocm.offer_name = :#planName
and cocm.obj_id0 = cri.obj_id0
and cocm.rec_id = cri.rec_id2
SELECT dis.TEAM_ID,
  nm.team_name,
  dis.call_type_id,
  dis.call_area_id,
  dis.call_sub_category_id,
  dis.call_category_id,
  res.call_resolution_id,
  act.call_action_id,
  que.QUEUE_ID
FROM oap_team_disp_params_mapping_t dis,
  oap_service_team_master_t nm ,
  oap_call_resolution_master_t res,
  oap_call_action_master_t act,
  oap_team_queue_map_t que
WHERE dis.SERVICE_GROUP      =:#serviceGroup
AND dis.CALL_SUB_CATEGORY    =:#callSubCategory
AND dis.CALL_TYPE            =:#callType
AND dis.CALL_AREA            =:#callArea
AND dis.CALL_CATEGORY        =:#callCategory
AND dis.team_id              = nm.team_id
AND res.call_sub_category_id = dis.call_sub_category_id
AND act.call_resolution_id   = res.call_resolution_id
AND que.team_id              = dis.TEAM_ID
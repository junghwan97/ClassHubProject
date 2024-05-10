package com.example.classhubproject.service.favorite;

import com.example.classhubproject.data.favorite.FavoriteDto;
import com.example.classhubproject.mapper.favorite.FavoriteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class FavoriteService {

    private final FavoriteMapper favoriteMapper;

    @Autowired
    public FavoriteService(FavoriteMapper favoriteMapper) {
        this.favoriteMapper = favoriteMapper;
    }

    public Map<String, Object> favoriteCommunity(FavoriteDto favorite) {
        Map<String, Object> result = new HashMap<>();
        result.put("favoriteCommunity", false);

        Integer deleteCnt = favoriteMapper.deleteFavoriteCommunity(favorite);

        if (deleteCnt != 1) {
            Integer insertCnt = favoriteMapper.favoriteCommunity(favorite);
            result.put("favoriteCommunity", true);
        }
        return result;
    }

    public Map<String, Object> favoriteComment(FavoriteDto favorite) {
        Map<String, Object> result = new HashMap<>();
        result.put("favoriteComment", false);

        Integer deleteCnt = favoriteMapper.deleteFavoriteComment(favorite);

        if (deleteCnt != 1) {
            Integer insertCnt = favoriteMapper.favoriteComment(favorite);
            result.put("favoriteComment", true);
        }
        return result;
    }
}
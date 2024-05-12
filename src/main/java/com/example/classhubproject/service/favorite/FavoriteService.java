package com.example.classhubproject.service.favorite;

import com.example.classhubproject.data.favorite.FavoriteRequestDTO;
import com.example.classhubproject.mapper.favorite.FavoriteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class FavoriteService {

    private final FavoriteMapper favoriteMapper;

    @Autowired
    public FavoriteService(FavoriteMapper favoriteMapper) {
        this.favoriteMapper = favoriteMapper;
    }

    public Integer favoriteCommunity(FavoriteRequestDTO favorite) {

        Integer deleteCnt = favoriteMapper.deleteFavoriteCommunity(favorite);

        if (deleteCnt != 1) {
            Integer insertCnt = favoriteMapper.favoriteCommunity(favorite);
            return insertCnt;
        } else {
            return 0;
        }
    }

    public Integer favoriteComment(FavoriteRequestDTO favorite) {

        Integer deleteCnt = favoriteMapper.deleteFavoriteComment(favorite);

        if (deleteCnt != 1) {
            Integer insertCnt = favoriteMapper.favoriteComment(favorite);
            return insertCnt;
        } else {
            return 0;
        }
    }

}
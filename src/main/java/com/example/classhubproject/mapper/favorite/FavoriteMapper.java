package com.example.classhubproject.mapper.favorite;

import com.example.classhubproject.data.favorite.FavoriteDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FavoriteMapper {
    int favoriteCommunity(FavoriteDto favorite);

    int favoriteComment(FavoriteDto favorite);

    int deleteFavoriteCommunity(FavoriteDto favorite);

    int deleteFavoriteComment(FavoriteDto favoriteDto);
}

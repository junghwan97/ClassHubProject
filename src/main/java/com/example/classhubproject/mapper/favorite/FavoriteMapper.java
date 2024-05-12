package com.example.classhubproject.mapper.favorite;

import com.example.classhubproject.data.favorite.FavoriteRequestDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FavoriteMapper {
    int favoriteCommunity(FavoriteRequestDTO favorite);

    int favoriteComment(FavoriteRequestDTO favorite);

    int deleteFavoriteCommunity(FavoriteRequestDTO favorite);

    int deleteFavoriteComment(FavoriteRequestDTO favoriteRequestDTO);
}

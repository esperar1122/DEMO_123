package com.campus.secondhand.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.secondhand.dto.ItemCreateRequest;
import com.campus.secondhand.entity.Item;
import com.campus.secondhand.entity.ItemImage;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.mapper.ItemImageMapper;
import com.campus.secondhand.mapper.ItemMapper;
import com.campus.secondhand.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemService {

    private final ItemMapper itemMapper;
    private final ItemImageMapper itemImageMapper;
    private final UserMapper userMapper;

    public ItemService(ItemMapper itemMapper, ItemImageMapper itemImageMapper, UserMapper userMapper) {
        this.itemMapper = itemMapper;
        this.itemImageMapper = itemImageMapper;
        this.userMapper = userMapper;
    }

    @Transactional
    public Item createItem(Long sellerId, ItemCreateRequest request) {
        if (request.getTitle() == null || request.getTitle().length() < 1 || request.getTitle().length() > 20) {
            throw new IllegalArgumentException("标题需1-20字");
        }
        if (request.getDescription() == null || request.getDescription().length() < 1 || request.getDescription().length() > 1000) {
            throw new IllegalArgumentException("描述需1-1000字");
        }
        if (request.getPrice() == null || request.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("价格需大于0");
        }
        if (request.getImages() != null && request.getImages().size() > 5) {
            throw new IllegalArgumentException("最多上传5张图片");
        }

        User seller = userMapper.selectById(sellerId);
        if (seller == null || seller.getStatus() == 1) {
            throw new IllegalArgumentException("用户不存在或已被封禁");
        }

        Item item = new Item();
        item.setSellerId(sellerId);
        item.setTitle(request.getTitle());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setStatus(0);
        itemMapper.insert(item);

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            for (int i = 0; i < request.getImages().size(); i++) {
                ItemImage image = new ItemImage();
                image.setItemId(item.getId());
                image.setImageUrl(request.getImages().get(i));
                image.setSortOrder(i);
                itemImageMapper.insert(image);
            }
        }

        return item;
    }

    public Item getItemById(Long id) {
        Item item = itemMapper.selectById(id);
        if (item == null || item.getDeleted() == 1) {
            return null;
        }
        return item;
    }

    public List<ItemImage> getItemImages(Long itemId) {
        return itemImageMapper.selectList(
            new QueryWrapper<ItemImage>()
                .eq("item_id", itemId)
                .orderByAsc("sort_order")
        );
    }

    public Page<Item> getActiveItems(int page, int size, String keyword) {
        QueryWrapper<Item> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 0).eq("deleted", 0);
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like("title", keyword).or().like("description", keyword));
        }
        wrapper.orderByDesc("created_at");
        return itemMapper.selectPage(new Page<>(page, size), wrapper);
    }

    public Page<Item> getItemsBySeller(Long sellerId, int page, int size) {
        QueryWrapper<Item> wrapper = new QueryWrapper<>();
        wrapper.eq("seller_id", sellerId).eq("deleted", 0);
        wrapper.orderByDesc("created_at");
        return itemMapper.selectPage(new Page<>(page, size), wrapper);
    }

    @Transactional
    public Item updateItem(Long itemId, Long userId, ItemCreateRequest request) {
        Item item = itemMapper.selectById(itemId);
        if (item == null || item.getDeleted() == 1) {
            throw new IllegalArgumentException("商品不存在");
        }
        if (!item.getSellerId().equals(userId)) {
            throw new IllegalArgumentException("无权限操作");
        }
        if (item.getStatus() != 0) {
            throw new IllegalArgumentException("该状态下不可修改");
        }

        if (request.getDescription() != null) {
            if (request.getDescription().length() < 1 || request.getDescription().length() > 1000) {
                throw new IllegalArgumentException("描述需1-1000字");
            }
            item.setDescription(request.getDescription());
        }
        
        itemMapper.updateById(item);
        
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            if (request.getImages().size() > 5) {
                throw new IllegalArgumentException("最多上传5张图片");
            }
            itemImageMapper.delete(new QueryWrapper<ItemImage>().eq("item_id", itemId));
            for (int i = 0; i < request.getImages().size(); i++) {
                ItemImage image = new ItemImage();
                image.setItemId(itemId);
                image.setImageUrl(request.getImages().get(i));
                image.setSortOrder(i);
                itemImageMapper.insert(image);
            }
        }

        return item;
    }

    @Transactional
    public void offlineItem(Long itemId, Long userId) {
        Item item = itemMapper.selectById(itemId);
        if (item == null || item.getDeleted() == 1) {
            throw new IllegalArgumentException("商品不存在");
        }
        if (!item.getSellerId().equals(userId)) {
            throw new IllegalArgumentException("无权限操作");
        }
        if (item.getStatus() != 0) {
            throw new IllegalArgumentException("该状态下无法下架");
        }

        item.setStatus(3);
        itemMapper.updateById(item);
    }
}
package io.openmarket.marketplace.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import io.openmarket.marketplace.model.Item;
import io.openmarket.marketplace.sql.QueryStatements;
import io.openmarket.mysql.dao.AbstractMySQLDao;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

@Log4j2
public class ItemDaoImpl extends AbstractMySQLDao implements ItemDao {
    @Inject
    public ItemDaoImpl(@Nonnull final ComboPooledDataSource source) {
        super(source);
    }

    protected boolean validate(@NonNull final Item obj) {
        if (obj.getItemName().isEmpty()) {
            return false;
        } else return obj.getStock() > 0;
    }

    public List<Integer> getItemIdsByOrg(@Nonnull final String orgId) {
        final List<Integer> result = new ArrayList<>();
        try {
            PreparedStatement getItemIDByOrgID = this.getSource().getConnection().prepareStatement(QueryStatements.GET_ITEMID_BY_ORGID);
            getItemIDByOrgID.clearParameters();
            getItemIDByOrgID.setString(1,orgId);
            ResultSet rs = getItemIDByOrgID.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("itemID");
                result.add(id);
            }
        } catch (SQLException e) {
            log.error("Failed to get items for orgId {}", orgId, e);
        }
        return result;
    }

    public List<Item> batchLoad(@Nonnull final Collection<Integer> itemIds) {
        final List<Item> result = new ArrayList<>();
        try {
            ResultSet rs;
            PreparedStatement getItemByID = this.getSource().getConnection().prepareStatement(QueryStatements.GET_ITEM_BY_ITEMID);
            getItemByID.clearParameters();
            for (Integer id : itemIds) {
                getItemByID.setInt(1, id);
                rs = getItemByID.executeQuery();
                if (!rs.next()) continue;
                result.add(sqlResultToItem(rs));
                rs.close();
            }

        } catch (SQLException e) {
            log.error("Failed to batch load items {}", itemIds, e);
        }
        return result;
    }

    private static Item sqlResultToItem(@NonNull final ResultSet rs) throws SQLException {
        return Item.builder().itemID(rs.getString("itemID"))
                .itemName(rs.getString("itemName"))
                .itemPrice(rs.getDouble("itemPrice"))
                .itemCategory(rs.getString("itemCategory"))
                .itemImageLink(rs.getString("itemImageLink"))
                .itemDescription(rs.getString("itemDescription"))
                .purchasedCount(rs.getInt("purchasedCount"))
                .stock(rs.getInt("stock"))
                .belongTo(rs.getString("belongTo")).build();
    }
}

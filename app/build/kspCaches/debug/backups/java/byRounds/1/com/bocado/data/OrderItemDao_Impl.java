package com.bocado.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.bocado.model.OrderItem;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class OrderItemDao_Impl implements OrderItemDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<OrderItem> __insertionAdapterOfOrderItem;

  private final EntityDeletionOrUpdateAdapter<OrderItem> __deletionAdapterOfOrderItem;

  private final EntityDeletionOrUpdateAdapter<OrderItem> __updateAdapterOfOrderItem;

  public OrderItemDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfOrderItem = new EntityInsertionAdapter<OrderItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `order_items` (`id`,`dishId`,`dishName`,`quantity`,`unitPrice`,`subtotal`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final OrderItem entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDishId());
        statement.bindString(3, entity.getDishName());
        statement.bindLong(4, entity.getQuantity());
        statement.bindDouble(5, entity.getUnitPrice());
        statement.bindDouble(6, entity.getSubtotal());
      }
    };
    this.__deletionAdapterOfOrderItem = new EntityDeletionOrUpdateAdapter<OrderItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `order_items` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final OrderItem entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfOrderItem = new EntityDeletionOrUpdateAdapter<OrderItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `order_items` SET `id` = ?,`dishId` = ?,`dishName` = ?,`quantity` = ?,`unitPrice` = ?,`subtotal` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final OrderItem entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDishId());
        statement.bindString(3, entity.getDishName());
        statement.bindLong(4, entity.getQuantity());
        statement.bindDouble(5, entity.getUnitPrice());
        statement.bindDouble(6, entity.getSubtotal());
        statement.bindLong(7, entity.getId());
      }
    };
  }

  @Override
  public Object insertOrderItem(final OrderItem item,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfOrderItem.insertAndReturnId(item);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertMultipleItems(final List<OrderItem> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfOrderItem.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOrderItem(final OrderItem item,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfOrderItem.handle(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateOrderItem(final OrderItem item,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfOrderItem.handle(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<OrderItem>> getAllOrderItems() {
    final String _sql = "SELECT * FROM order_items";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"order_items"}, new Callable<List<OrderItem>>() {
      @Override
      @NonNull
      public List<OrderItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDishId = CursorUtil.getColumnIndexOrThrow(_cursor, "dishId");
          final int _cursorIndexOfDishName = CursorUtil.getColumnIndexOrThrow(_cursor, "dishName");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfUnitPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "unitPrice");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final List<OrderItem> _result = new ArrayList<OrderItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final OrderItem _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpDishId;
            _tmpDishId = _cursor.getInt(_cursorIndexOfDishId);
            final String _tmpDishName;
            _tmpDishName = _cursor.getString(_cursorIndexOfDishName);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            final double _tmpUnitPrice;
            _tmpUnitPrice = _cursor.getDouble(_cursorIndexOfUnitPrice);
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            _item = new OrderItem(_tmpId,_tmpDishId,_tmpDishName,_tmpQuantity,_tmpUnitPrice,_tmpSubtotal);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getOrderItemById(final int id, final Continuation<? super OrderItem> $completion) {
    final String _sql = "SELECT * FROM order_items WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<OrderItem>() {
      @Override
      @Nullable
      public OrderItem call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDishId = CursorUtil.getColumnIndexOrThrow(_cursor, "dishId");
          final int _cursorIndexOfDishName = CursorUtil.getColumnIndexOrThrow(_cursor, "dishName");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfUnitPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "unitPrice");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final OrderItem _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpDishId;
            _tmpDishId = _cursor.getInt(_cursorIndexOfDishId);
            final String _tmpDishName;
            _tmpDishName = _cursor.getString(_cursorIndexOfDishName);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            final double _tmpUnitPrice;
            _tmpUnitPrice = _cursor.getDouble(_cursorIndexOfUnitPrice);
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            _result = new OrderItem(_tmpId,_tmpDishId,_tmpDishName,_tmpQuantity,_tmpUnitPrice,_tmpSubtotal);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

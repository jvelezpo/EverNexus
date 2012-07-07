class CreateUsers < ActiveRecord::Migration
  def change

    if table_exists? :users
      drop_table :users
    end

    create_table :users do |t|
      t.string :nombre
      t.string :email

      t.timestamps
    end
  end
end

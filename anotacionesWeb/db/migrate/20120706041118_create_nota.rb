class CreateNota < ActiveRecord::Migration
  def change

    if table_exists? :nota
      drop_table :nota
    end

    create_table :nota do |t|
      t.integer :user_id
      t.text :nota

      t.timestamps
    end
  end
end

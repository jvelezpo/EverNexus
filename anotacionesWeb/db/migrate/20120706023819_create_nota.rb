class CreateNota < ActiveRecord::Migration
  def change
    create_table :nota do |t|
      t.string :user
      t.text :nota

      t.timestamps
    end
  end
end

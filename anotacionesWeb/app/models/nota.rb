class Nota < ActiveRecord::Base
  attr_accessible :user_id, :nota

  belongs_to :user

  validates :user_id, :presence  => true
  validates :nota, :presence  => true, :length => {:within => 10..1000}
end

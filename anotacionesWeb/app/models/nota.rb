class Nota < ActiveRecord::Base
  attr_accessible :user, :nota

  validates :user, :presence  => true, :uniqueness => true
  validates :nota, :presence  => true, :length => {:within => 10..1000}
end

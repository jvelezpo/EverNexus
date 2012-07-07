class User < ActiveRecord::Base
  attr_accessible :nombre, :email

  has_many :notas, :dependent => :destroy

  email_regex = /[\w+\-.]+@[a-z\d\-.]+\.[a-z]+/i

  validates :nombre, :presence  => true, :length => {:within => 4..40}, :uniqueness => {:case_sensitive => false}
  validates :email, :presence   => true,
            :format     => {:with => email_regex},
            :uniqueness => {:case_sensitive => false}
end

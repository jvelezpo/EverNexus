class User < ActiveRecord::Base
  has_one :text

  validates :name, :presence => true,
            :length   => {:maximum => 50}

  validates :email, :presence   => true,
            :uniqueness => {:case_sensitive => false}

  validates :pass, :presence     => true,
            :length => {:within => 6..40}


  def User.authenticate(name, submitted_password)
    user = User.find_by_name(name)
    return nil if user.nil?
    return user if user.pass.eql?submitted_password
    nil
  end
end
